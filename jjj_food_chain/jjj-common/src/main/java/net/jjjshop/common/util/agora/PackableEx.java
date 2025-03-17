package net.jjjshop.common.util.agora;

public interface PackableEx extends Packable {
    void unmarshal(ByteBuf in);
}
