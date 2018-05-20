package io.github.ganchix.arangodb;

import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static io.github.ganchix.arangodb.GanacheConstants.IMAGE;
import static io.github.ganchix.arangodb.GanacheConstants.LATEST_VERSION;

@Slf4j
public class GanacheContainer<SELF extends GanacheContainer<SELF>> extends GenericContainer<SELF> {

    private static final Object DRIVER_LOAD_MUTEX = new Object();
    List<String> options = new ArrayList<>();
    private Web3j web3j;
    private Integer port = 8545;

    public GanacheContainer() {
        this(LATEST_VERSION);
    }

    public GanacheContainer(String version) {
        super(IMAGE + ":" + version);
    }

    @Override
    protected void configure() {
        withExposedPorts(port);
        withLogConsumer(new Slf4jLogConsumer(log));
        if (options.size() > 0) {
            withCommand(String.join(" ", options));
        }
    }

    /**
     * -n or --secure: Lock available accounts by default (good for third party transaction signing)
     * -m or --mnemonic: Use a specific HD wallet mnemonic to generate initial addresses.
     * -s or --seed: Use arbitrary data to generate the HD wallet mnemonic to be used.
     * -g or --gasPrice: Use a custom Gas Price (defaults to 20000000000)
     * -l or --gasLimit: Use a custom Gas Limit (defaults to 90000)
     * -f or --fork: Fork from another currently running Ethereum client at a given block. Input should be the HTTP location and port of the other client, e.g. http://localhost:8545. You can optionally specify the block to fork from using an @ sign: http://localhost:8545@1599200.
     * -i or --networkId: Specify the network id the ganache-cli will use to identify itself (defaults to the current time or the network id of the forked blockchain if configured)
     * --noVMErrorsOnRPCResponse: Do not transmit transaction failures as RPC errors. Enable this flag for error reporting behaviour which is compatible with other clients such as geth and Parity.
     * Working on it
     */

    public SELF withNumberAccounts(Integer accounts) {
        String option = "--accounts ".concat(accounts.toString());
        options.add(option);
        return self();
    }

    //TODO: Fix
    public SELF withDefaultBalanceEther(BigDecimal defaultBalanceEther) {
        String option = "--defaultBalanceEther ".concat(defaultBalanceEther.toString());
        options.add(option);
        return self();
    }

    public SELF withBlockTime(BigInteger blockTime) {
        String option = "--blockTime ".concat(blockTime.toString());
        options.add(option);
        return self();
    }

    public SELF withDeterministic() {
        options.add("--deterministic");
        return self();
    }

    public SELF withPort(Integer port) {
        this.port = port;
        options.add("--port ".concat(port.toString()));
        return self();
    }

    public SELF debug() {
        options.add("--debug");
        return self();
    }

    public SELF memoryUsage() {
        options.add("--mem");
        return self();
    }

    public Web3j getWeb3j() {
        synchronized (DRIVER_LOAD_MUTEX) {
            if (web3j == null) {
                try {
                    web3j = Web3j.build(new HttpService("http://" + getContainerIpAddress() + ":" + getMappedPort(port) + "/"));
                } catch (Exception e) {
                    throw new RuntimeException("Could not get Web3j", e);
                }
            }
        }

        return web3j;

    }


}
