package io.github.ganchix.ganache;

import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import org.junit.Test;
import org.web3j.protocol.Web3j;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Slf4j
public class GanacheContainerTest {


    @Rule
    public GanacheContainer ganacheContainer = new GanacheContainer().debug().withPort(1485)
            .withNumberAccounts(2)
            .withDefaultBalanceEther(new BigInteger(String.valueOf(1258)))
            .withNetworkId(10L);

    public GanacheContainerTest() throws ParseException {
    }

    @Test
    public void simpleTestWithClientCreation() throws IOException {
        Web3j web3j = ganacheContainer.getWeb3j();
        assertEquals( web3j.ethBlockNumber().send().getBlockNumber(), BigInteger.ZERO);
        assertNotNull(ganacheContainer);
    }


}
