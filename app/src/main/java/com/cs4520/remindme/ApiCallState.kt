package com.cs4520.remindme

enum class ApiCallState {
    REQUESTED, // an api request is in progress
    SUCCESS, // the api request returned entries
    EMPTY, // the api request returned but had 0 entries
    ERROR, // the api request returned but returned an error
    FAILURE // the api request did not return
}