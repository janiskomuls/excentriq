//package com.excentriq.stocks
//
//import zio._
//
//object Main extends ZIOAppDefault {
//
//  val applicationLogic =
//    for {
//      _ <- FetchDividendsHistory.apply()
//    } yield ()
//
//  val run =
//    applicationLogic.provide(
//      githubSubcomponent
//    )
//
////  val githubSubcomponent: ZLayer[Any, Nothing, Github] =
////    ZLayer.make[Github](
////      Github.live,
////      Http.live,
////      Config.default
////    )
//}
