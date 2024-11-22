package br.com.fz.Bookmatch.service;

public interface IConverteDados {
    <T> T obterDados (String json, Class<T> classe);
}
