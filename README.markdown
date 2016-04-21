[![Build Status](https://travis-ci.org/etorreborre/specs2.png?branch=master)](https://travis-ci.org/etorreborre/specs2)

Installation instructions
=========================

[![Join the chat at https://gitter.im/cfreeman/specs2](https://badges.gitter.im/cfreeman/specs2.svg)](https://gitter.im/cfreeman/specs2?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

You need to download and install sbt (simple build tool)
Then execute the following command:

        > sbt update publish-local

Then you can generate the User Guide with:

        sbt
        > test-only org.specs2.UserGuide -- html

This should create html files in the target/specs2-reports directory. 
