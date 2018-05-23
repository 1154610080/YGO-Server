package com.ygo.controller;

import com.ygo.constant.StatusCode;
import com.ygo.model.DataPacket;
import com.ygo.model.ResponseStatus;
import com.ygo.util.CommonLog;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.ipfilter.AbstractRemoteAddressFilter;

import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;


public class IpFilterHandler extends AbstractRemoteAddressFilter<InetSocketAddress>{

    private static List<String> filteredAddresses = new LinkedList<>();

    public static void addFilteredAddress(Channel channel) throws InterruptedException {
        String address = ((InetSocketAddress)channel.remoteAddress())
                .getHostString();
        if(!filteredAddresses.contains(address)){
            filteredAddresses.add(address);
            CommonLog.log.info(
                    "The address " + address + " has been add the blacklist " + filteredAddresses.size());
        }
        if(channel.isActive())
            channel.close();

    }

    @Override
    protected boolean accept(ChannelHandlerContext channelHandlerContext, InetSocketAddress inetSocketAddress) throws Exception {
        if(filteredAddresses.contains(inetSocketAddress.getHostString())){
            DataPacket packet = new DataPacket(new ResponseStatus(StatusCode.BLACKLISTED));
            channelHandlerContext.channel().writeAndFlush(packet);
            CommonLog.log.info(inetSocketAddress.getHostString() + " has been rejected.");
            return false;
        }
        return true;
    }

    @Override
    protected ChannelFuture channelRejected(ChannelHandlerContext ctx, InetSocketAddress remoteAddress) {
        DataPacket packet = new DataPacket(new ResponseStatus(StatusCode.BLACKLISTED));
        ctx.channel().writeAndFlush(packet);
        return super.channelRejected(ctx, remoteAddress);
    }
}
