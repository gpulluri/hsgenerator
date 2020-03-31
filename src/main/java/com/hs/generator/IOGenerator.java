package com.hs.generator;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.List;

/**
 * Created by gpulluri on 5/18/17.
 */
public class IOGenerator {

    public static TypeSpec generateConnector() {

        ClassName eventLoopGroup = ClassName.get("io.netty.channel", "EventLoopGroup");
        ClassName nioEventLoopGroup = ClassName.get("io.netty.channel.nio", "NioEventLoopGroup");
        ClassName bootstrap = ClassName.get("io.netty.bootstrap", "Bootstrap");
        ClassName channelFuture = ClassName.get("io.netty.channel", "ChannelFuture");
        ClassName channelInitializer = ClassName.get("io.netty.channel", "ChannelInitializer");
        ClassName channelOption = ClassName.get("io.netty.channel", "ChannelOption");
        ClassName socketChannel = ClassName.get("io.netty.channel.socket", "SocketChannel");
        ClassName nioSocketChannel = ClassName.get("io.netty.channel.socket.nio", "NioSocketChannel");

        //TypeSpec.Builder ciBuilder = TypeSpec.classBuilder("ClientChannelInitializer")


        MethodSpec connectMethod = MethodSpec.methodBuilder("connect")
                .addParameter(String.class, "host")
                .addParameter(int.class, "port")
                .addStatement("$T workerGroup = new $T()", eventLoopGroup, nioEventLoopGroup)
                .addCode("try {\n")
                .addStatement("$T b = new $T()",bootstrap, bootstrap)
                .addStatement("b.group(workerGroup)")
                .addStatement("b.channel($T)", nioSocketChannel)
                .addStatement("b.option($T.SO_KEEPALIVE, true)",channelOption)
                .addStatement("b.handler(new $T<$T>() {", channelInitializer, socketChannel)
                /*.addStatement()

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new MessageDecoder(), new IncomingMessageHandler());
                    }
                });)*/
                .addCode("} finally {\n")
                .addCode("}\n")
                .addException(InterruptedException.class)
                .addModifiers(Modifier.PUBLIC)
                .build();

        TypeSpec connector = TypeSpec.classBuilder("Connector")
                .addMethod(connectMethod)
                .addModifiers(Modifier.PUBLIC)
                .build();

        return connector;
    }

    public static TypeSpec generateAcceptor() {


        return null;
    }

    public static TypeSpec generateMessageEncoder() {

        MethodSpec decodeMethod = MethodSpec.methodBuilder("write")
                .addAnnotation(AnnotationSpec.builder(Override.class).build())
                .addParameter(ClassName.get("io.netty.channel", "ChannelHandlerContext"), "ctx")
                .addParameter(Object.class, "msg")
                .addParameter(ClassName.get("io.netty.channel", "ChannelPromise"), "promise")
                .addException(Exception.class)
                .addModifiers(Modifier.PUBLIC)
                .build();

        TypeSpec encoder = TypeSpec.classBuilder("MessageEncoder")
                .superclass(ClassName.get("io.netty.channel","ChannelOutboundHandlerAdapter"))
                .addMethod(decodeMethod)
                .addModifiers(Modifier.PUBLIC)
                .build();

        return encoder;
    }

    public static TypeSpec generateMessageHandler() {

        return null;
    }

    public static TypeSpec generateMessageDecoder() {

        MethodSpec decodeMethod = MethodSpec.methodBuilder("decode")
                .addParameter(ClassName.get("io.netty.channel", "ChannelHandlerContext"), "ctx")
                .addParameter(ClassName.get("io.netty.buffer", "ByteBuf"), "in")
                .addParameter(ParameterizedTypeName.get(List.class, Object.class),"out")
                .addException(Exception.class)
                .addModifiers(Modifier.PROTECTED)
                .build();

        TypeSpec decoderBuilder = TypeSpec.classBuilder("MessageDecoder")
                .superclass(ClassName.get("io.netty.handler.codec","ByteToMessageDecoder"))
                .addMethod(decodeMethod)
                .addModifiers(Modifier.PUBLIC)
                .build();
        return decoderBuilder;
    }
}
