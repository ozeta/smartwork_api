package dev.oz.smartworkapi.repository;

import dev.oz.smartworkapi.dto.Request;
import io.vertx.core.Future;
import io.vertx.core.Promise;

import java.util.function.Function;

public interface IRequest {




  class RequestRepositoryException extends Exception {
  }

  class DeleteRequestRepositoryException extends RequestRepositoryException {
  }

  class PutRequestRepositoryException extends RequestRepositoryException {
  }

  class PostRequestRepositoryException extends RequestRepositoryException {
  }

  class GetRequestRepositoryException extends RequestRepositoryException {
  }
}
