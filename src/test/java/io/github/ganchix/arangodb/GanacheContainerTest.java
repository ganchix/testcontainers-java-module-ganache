package io.github.ganchix.arangodb;

import org.junit.Rule;
import org.junit.Test;
import org.web3j.protocol.Web3j;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GanacheContainerTest {


    @Rule
    public GanacheContainer ganacheContainer = new GanacheContainer().debug().withPort(1485)
            .withNumberAccounts(2)
            .withDefaultBalanceEther(new BigDecimal(1258.4842));

    @Test
    public void simpleTestWithClientCreation() throws IOException {
        Web3j web3j = ganacheContainer.getWeb3j();
        assertEquals( web3j.ethBlockNumber().send().getBlockNumber(), BigInteger.ZERO);
        assertNotNull(ganacheContainer);
    }


}
