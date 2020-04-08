Propositional machine palette for BeepBeep 3
============================================

This project is an extension to the [BeepBeep
3](https://liflab.github.io/beepbeep-3), event stream processing engine,
called a *palette*, that provides functionalities to create and
manipulate **propositional machines**.

What is this?
-------------

Please refer to the following research paper for detailed information and
examples of what this extension can do.

> R. Taleb, R. Khoury, S. Hallé. *Runtime Verification under Access Control
> Constraints*. In preparation.

Further documentation might be added to this Readme some time in the future
(any help is welcome!).

Building this palette
---------------------

To compile the palette, make sure you have the following:

- The Java Development Kit (JDK) to compile. The palette complies
  with Java version 6; it is probably safe to use any later version.
- [Ant](http://ant.apache.org) to automate the compilation and build process

The palette also requires the following Java libraries:

- The latest version of [BeepBeep 3](https://liflab.github.io/beepbeep-3)

These dependencies can be automatically downloaded and placed in the
`dep` folder of the project by typing:

    ant download-deps

From the project's root folder, the sources can then be compiled by simply
typing:

    ant

This will produce a file called `prop-machine.jar` in the folder. This file
is *not* runnable and stand-alone. It is meant to be used in a Java project
alongside `beepbeep-3.jar`.

<!-- :maxLineLen=78: -->
