package com.solution.threadlocal;

import com.solution.model.User;

public class UserThreadLocalHold {
    public static ThreadLocal<User> threadLocal = new ThreadLocal<>();
}