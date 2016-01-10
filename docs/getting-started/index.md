# Getting Started

This guide is intended for those who wish to:

* Contribute code to Visallo
* Create their own Visallo plugins
* Create a custom build of Visallo

In order to work with Visallo as a developer, it's recommended that:

* You know Java, since the entire backend is written in Java.
* You know JavaScript, as the UI is very JavaScript heavy.
* You're familiar with common big data tools, including the Hadoop stack, Accumulo.

If that sounds like you, then please keep in mind the following conventions you'll encounter while reading this guide.

* Any reference to `$PROJECT_DIR` refers to the directory into which you've cloned the Visallo source code. Unless otherwise stated, all commands are run from this directory.

## Understand the [Architecture](architecture-overview.md)

Visallo has a lot of components but an easy to understand architecture. Learning a little about the core components
discussed in this section will help you better understand the project.

## Get the [Source Code](source-code.md)

Before you can get started, you'll need to get a copy of the Visallo source code. This section explains how to do that
and a little about the source code structure.

## Set up your [Development Environment](development-environment.md)

Debugging and developing for Visallo is easiest using an IDE. These instructions will help you get that setup.

## Get the Web Server [Running](running.md)

Visallo has a lot of dependencies, some of which are required while others are not. This section will help you decide
which dependencies you need to install for the features you want.

## Learn the [Build Options](build.md)

Visallo uses Maven as the build system. There are a number of build options depending on what you want to run. This
section will help you understand the options.

## Create an [Ontology](ontology.md)

Visallo uses OWL files to define what type of entities you can create, what properties they can have and what they
can connect to.

## [Getting Help](help.md)

Running Visallo is a challenging, but rewarding, endeavor. Learn about your options for getting help in this section.

## [Contributing](contributing.md)

Once you've made changes that you want to share with the community, the next step is to submit those changes back via a
pull request.
