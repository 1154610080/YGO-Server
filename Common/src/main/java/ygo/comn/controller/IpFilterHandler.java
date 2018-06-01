package ygo.comn.controller;

import ygo.comn.constant.StatusCode;
import ygo.comn.model.DataPacket;
import ygo.comn.model.ResponseStatus;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.ipfilter.AbstractRemoteAddressFilter;
import ygo.comn.util.YgoLog;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;


public class IpFilterHandler extends AbstractRemoteAddressFilter<InetSocketAddress>{

    private static List<String> filteredAddresses = new LinkedList<>();

    private static YgoLog log = new YgoLog("IPFilter");

    public static void addFilteredAddress(Channel channel) throws InterruptedException {
        String address = ((InetSocketAddress)channel.remoteAddress())
                .getHostString();
        if(!filteredAddresses.contains(address)){
            filteredAddresses.add(address);
            log.warn(StatusCode.BLACKLISTED,
                    "The address " + address + " has been add the blacklist " + filteredAddresses.size());
        }
        DataPacket packet = new DataPacket(
                new ResponseStatus()
        );

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
            log.warn(StatusCode.BLACKLISTED, inetSocketAddress.getHostString() + " has been rejected.");
            return false;
        }
        return true;
    }


}
