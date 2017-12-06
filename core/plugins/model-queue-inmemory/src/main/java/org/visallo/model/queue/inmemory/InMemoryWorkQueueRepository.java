package org.visallo.model.queue.inmemory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONObject;
import org.vertexium.Graph;
import org.visallo.core.config.Configuration;
import org.visallo.core.ingest.WorkerSpout;
import org.visallo.core.ingest.WorkerTuple;
import org.visallo.core.model.WorkQueueNames;
import org.visallo.core.model.workQueue.Priority;
import org.visallo.core.model.workQueue.WorkQueueRepository;

import java.util.*;
import org.visallo.core.status.model.QueueStatus;
import org.visallo.core.status.model.Status;

/**
 *
 */

@Singleton
public class InMemoryWorkQueueRepository extends WorkQueueRepository {

  private static Map<String, Deque<byte[]>> queues = new ConcurrentHashMap<>();
  private List<BroadcastConsumer> broadcastConsumers = new ArrayList<>();
  private ExecutorService executor = Executors.newFixedThreadPool(10);// hard code and default is ten

  @Inject
  public InMemoryWorkQueueRepository(
      Graph graph,
      WorkQueueNames workQueueNames,
      Configuration configuration
  ) {
    super(graph, workQueueNames, configuration);

  }

  /**
   * make the job run background, so the api call may return more faster.
   */

  @Override
  protected void broadcastJson(JSONObject json) {
    executor.execute(new Runnable() {
      @Override
      public void run() {
        for (BroadcastConsumer consumer : broadcastConsumers) {
          consumer.broadcastReceived(json);
        }

      }
    });

  }

  @Override
  public void pushOnQueue(String queueName, byte[] data, Priority priority) {
    LOGGER.debug("push on queue: %s: %s", queueName, data);
    addToQueue(queueName, data, priority);
  }

  public void addToQueue(String queueName, byte[] data, Priority priority) {
    final Deque<byte[]> queue = getQueue(queueName);
    if (priority == Priority.HIGH) {
      queue.addFirst(data);// add to head
    } else {
      queue.add(data); // add to tail
    }
  }

  @Override
  public void flush() {

  }

  @Override
  public void format() {
    clearQueue();
  }

  @Override
  public void subscribeToBroadcastMessages(BroadcastConsumer broadcastConsumer) {
    broadcastConsumers.add(broadcastConsumer);
  }

  // @Override
  public void unsubscribeFromBroadcastMessages(BroadcastConsumer broadcastConsumer) {
    broadcastConsumers.remove(broadcastConsumer);
  }

  @Override
  public WorkerSpout createWorkerSpout(String queueName) {
    final Deque<byte[]> queue = getQueue(queueName);
    return new WorkerSpout() {
      @Override
      public WorkerTuple nextTuple() throws Exception {
        if (queue.isEmpty()) {
          return null;
        }
        byte[] entry = queue.pollFirst();
        if (entry == null) {
          return null;
        }
        return new WorkerTuple("", entry);

      }
    };
  }

  @Override
  public Map<String, Status> getQueuesStatus() {
    Map<String, Status> results = new HashMap<>();
    for (Entry<String, Deque<byte[]>> queue : queues.entrySet()) {
      results.put(queue.getKey(), new QueueStatus(queue.getValue().size()));
    }
    return results;
  }

  public static void clearQueue() {
    queues.clear();
  }

  @Override
  protected void deleteQueue(String queueName) {
    queues.remove(queueName);
  }

  public static Deque<byte[]> getQueue(String queueName) {
    return queues.computeIfAbsent(queueName, k -> new ConcurrentLinkedDeque<>());
  }
}
