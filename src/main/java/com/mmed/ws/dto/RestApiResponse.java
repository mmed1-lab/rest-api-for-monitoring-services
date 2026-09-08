package com.mmed.ws.dto;

public record RestApiResponse<T>(boolean success, String message, T data) {}
