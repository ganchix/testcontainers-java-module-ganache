package io.github.ganchix.ganache;

import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static io.github.ganchix.ganache.GanacheConstants.IMAGE;
import static io.github.ganchix.ganache.GanacheConstants.LATEST_VERSION;

@Slf4j
public class GanacheContainer<SELF extends GanacheContainer<SELF>> extends GenericContainer<SELF> {

    private static final Object DRIVER_LOAD_MUTEX = new Object();
    private List<String> options = new ArrayList<>();
    private Web3j web3j;
    private Integer port = 8545;

    private List<Account> accounts = new ArrayList<>();

    public GanacheContainer() {
        this(LATEST_VERSION);
    }

    public GanacheContainer(String version) {
        super(IMAGE + ":" + version);
    }

    public void addAccountAddress(Integer position, String address) {
        accounts.add(position, Account.builder().address(address).build());
    }

    public void addAccountPrivateKey(Integer position, String privateKey) {
        Account account = accounts.get(position);
        account.setPrivateKey(privateKey);
        account.setCredential(Credentials.create(privateKey, account.getAddress()));
    }

    public List<Account> getAccounts() {
        return this.accounts;
    }

    @Override
    protected void configure() {
        withExposedPorts(port);
        withLogConsumer(new LogGanacheExtractorConsumer(log, this));
        if (options.size() > 0) {
            withCommand(String.join(" ", options));
        }
    }

    /**
     * -n or --secure: Lock available accounts by default (good for third party transaction signing)
     * -m or --mnemonic: Use a specific HD wallet mnemonic to generate initial addresses.
     * -s or --seed: Use arbitrary data to generate the HD wallet mnemonic to be used.
     * -f or --fork: Fork from another currently running Ethereum client at a given block. Input should be the HTTP location and port of the other client, e.g. http://localhost:8545. You can optionally specify the block to fork from using an @ sign: http://localhost:8545@1599200.
     * --noVMErrorsOnRPCResponse: Do not transmit transaction failures as RPC errors. Enable this flag for error reporting behaviour which is compatible with other clients such as geth and Parity.
     * Working on it
     */

    public SELF withNumberAccounts(Integer accounts) {
        String option = "--accounts ".concat(accounts.toString());
        options.add(option);
        return self();
    }

    //TODO: Fix
    public SELF withDefaultBalanceEther(BigInteger defaultBalanceEther) {
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


    public SELF withGasPrice(BigInteger gasPrice) {
        String option = "--gasPrice ".concat(gasPrice.toString());
        options.add(option);
        return self();
    }

    public SELF withGasLimit(BigInteger gasLimit) {
        String option = "--gasLimit ".concat(gasLimit.toString());
        options.add(option);
        return self();
    }

    public SELF withNetworkId(Long networkId) {
        String option = "--networkId ".concat(networkId.toString());
        options.add(option);
        return self();
    }

    public SELF withFork(String location) {
        if (!location.startsWith("http")) {
            throw new RuntimeException("Location must start with http");
        }
        String option = "--fork ".concat(location);
        options.add(option);
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
