Simplest Unfiltered
===================

[![Build Status](https://travis-ci.org/thetrav/simplest-unfiltered.svg?branch=master)](https://travis-ci.org/thetrav/simplest-unfiltered)

A very simple example of a RESTful http service.

Json => json4s

http server implementation => netty

http routing => unfiltered

database interaction => slick

database implementation => h2

To Run
======

    sbt run
    
    
Endpoints
=========

http://localhost:8080/people

GET => List

POST => Create

DELETE /<name> => Delete


