package com.fm.vertx.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class HelloFuture extends AbstractVerticle {

	
		public static void main(String[] args) {
			Vertx vertx = Vertx.vertx();
			Promise<String> dbVerticleDeployment = Promise.promise();
			vertx.deployVerticle(new HelloFuture(), dbVerticleDeployment);
		}
	
		@Override
		public void start(Promise<Void> promise) throws Exception {
			Promise<String> httpVerticleDeployment = Promise.promise();
			sayHello("Vertx", ar -> {
				promise.complete();
			});
			 
			System.out.println("last line of start");
		};
			
			
		
	public void sayHello(String name,  Handler<AsyncResult> aHandler) {

		Future future = Future.future();
		future.setHandler(aHandler);

		Future hello = Future.future();
		concatWithHello(name, hello);

		hello.compose(v -> {
			Future exclamation = Future.future();

			concatWithExclamation(v.toString(), exclamation.completer());
			return exclamation;

		}).compose(v -> {
			concatWithISay(v.toString(), future.completer());
			System.out.println("TTT - "+v);
		}, future);
		
		
		System.out.println("UUUUUUUUUU");
	}

	private void concatWithHello(String aName, Handler<AsyncResult> aHandler) {
		System.out.println("1111");
		aHandler.handle(Future.succeededFuture("Hello " + aName));

	}

	private void concatWithExclamation(String aHello, Handler<AsyncResult> aHandler) {
		System.out.println("2222");
		aHandler.handle(Future.succeededFuture(aHello + "!"));

	}

	private void concatWithISay(String aString, Handler<AsyncResult> aHandler) {
		System.out.println("3333");
		aHandler.handle(Future.succeededFuture("I say: \"" + aString + "\""));

	}
}
