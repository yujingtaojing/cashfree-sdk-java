package com.cashfree.lib.utils;
import com.cashfree.lib.exceptions.*;

public class ExceptionThrower {
  public static void throwException(int resCode, String requestId, String message) {
    String msg = "Message : "+ message + " | X-Request-Id: " + requestId;
    switch (resCode) {
      case 400:
        throw new BadRequestException(msg);
      case 401:
        throw new AuthenticationFailureException(msg);
      case 403:
        throw new ForbiddenException(msg);
      case 404:
        throw new EntityDoesntExistException(msg);
      case 405:
        throw new MethodNotAllowedException(msg);
      case 409:
        throw new AlreadyExistException(msg);
      case 412:
        throw new PreconditionFailedException(msg);
      case 413:
        throw new RequestTooLargeException(msg);
      case 422:
        throw new InputWrongFormatException(msg);
      case 424:
        throw new RequestFailedException(msg);
      case 429:
        throw new TooManyRequestException(msg);
      case 500:
        throw new InternalServerException(msg);
      case 503:
        throw new ServiceDownException(msg);
      default:
        throw new UnknownExceptionOccured(msg);
    }
    }
}
