Simplest Unfiltered
===================

[![Build Status](https://travis-ci.org/thetrav/simplest-unfiltered.svg?branch=master)](https://travis-ci.org/thetrav/simplest-unfiltered)

A very simple example of a RESTful http service using unfiltered on top of netty so I can show people

There is a h2 database running in memory and I'm just using JDBC to interact with it, 
yes I know a real library would be better but I didn't want to load up on dependencies,
It's bad enough that I've got a JSON library in there (feel free to swap it with argonaut, or PlayJSON or something else)


To Run
======

    sbt run
    
    
Endpoints
=========

http://localhost:8080/people

GET => List

POST => Create

DELETE /<name> => Delete


