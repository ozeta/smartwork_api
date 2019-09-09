package dev.oz.smartworkapi.repository;

import dev.oz.smartworkapi.dto.Request;
import io.vertx.core.Future;

public interface IRequest {


  Future<Request> getRequest(int result_id);
  Future<Integer> deleteRequest(int id);
  Future<Request> putRequest(Request request);
  Future<Request> postRequest(Request request);

}
