package com.fm.vertx.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

public class Hello extends AbstractVerticle {

	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new Hello(), res -> {
			if (res.succeeded()) {
				System.out.println("OK");
			} else {
				System.out.println("FAIL");
			}
		});
	}

	@Override
	public void start(Promise<Void> promise) throws Exception {
		sayHello("Vertx", ar -> {
			if (ar.succeeded()) {
				System.out.println(ar.result());
				promise.complete();
			} else {
				promise.fail("Something went wrong");
			}
		});
		
		System.out.println("last line of start");
	}

	public void sayHello(String name, Handler<AsyncResult<String>> aHandler) {
         
		concatWithHello(name, hello -> {
			if (hello.succeeded()) {
				concatWithExclamation(hello.result(), exclamation -> {
					if (exclamation.succeeded()) {
						concatWithISay(exclamation.result(), aHandler);
					} else {
						aHandler.handle(Future.failedFuture(hello.cause())); // <-- see the arrow head?
					}
				});
			} else {
				aHandler.handle(Future.failedFuture(hello.cause()));
			}
		});
	}

	private void concatWithHello(String aName, Handler<AsyncResult<String>> aHandler) {
		String str = "Hello " + aName;
		Future<String> fut = Future.succeededFuture(str);
		aHandler.handle(fut);

	}

	private void concatWithExclamation(String aHello, Handler<AsyncResult<String>> aHandler) {
		aHandler.handle(Future.succeededFuture(aHello + "!"));

	}

	private void concatWithISay(String aString, Handler<AsyncResult<String>> aHandler) {
		aHandler.handle(Future.succeededFuture("I say: \"" + aString + "\""));
	}
}
