package ru.springproject.utils;

public class Views {
    public static class OrderDetail {}
    public static class FullDetail extends OrderDetail {};
    public static class FullDetailWithId extends FullDetail {};
    public static class Internal extends FullDetailWithId {}
}
