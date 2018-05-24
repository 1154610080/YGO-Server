package ygo.comn.controller;

import ygo.comn.model.DataPacket;
import ygo.comn.model.ResponseStatus;
import ygo.comn.util.CommonLog;
import io.netty.channel.Channel;
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
        DataPacket packet = new DataPacket(
                new ResponseStatus()
        );
//        if(channel.isActive())
//            channel.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        Channel channel = ctx.channel();
        String hostString = ((InetSocketAddress)channel.remoteAddress()).getHostString();

        if(filteredAddresses.contains(hostString))
            channel.close();
    }

    @Override
    protected boolean accept(ChannelHandlerContext channelHandlerContext, InetSocketAddress inetSocketAddress) throws Exception {
        if(filteredAddresses.contains(inetSocketAddress.getHostString())){
            CommonLog.log.info(inetSocketAddress.getHostString() + " has been rejected.");
            return false;
        }
        return true;
    }


}
