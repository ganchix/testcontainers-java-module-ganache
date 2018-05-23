package io.github.ganchix.ganache;

import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Slf4j
public class GanacheContainerTest {

	private String PRIVATE_KEY_0 = "ae020c8ddb6fbc24e167b011666639d2ce3d4aa0d9c13d02d726d6865618a781";
	private String PRIVATE_KEY_1 = "81ad1ba5c4da47feb0f0163c0c61a66c4d0e6a66bd839827444b1e3362016140";


	@Rule
	public GanacheContainer ganacheContainer = getDefaultGanacheContainer();

	private GanacheContainer getDefaultGanacheContainer() {
		return new GanacheContainer()
				.withDebug()
				.withPort(1485)
				.withNumberAccounts(2)
				.withDefaultBalanceEther(new BigInteger(String.valueOf(1258)))
				.withNetworkId(10L)
				.withBlockTime(BigInteger.ONE)
				.withMemoryUsage()
				.withSecure()
				.withNoVMErrorsOnRPCResponse()
				.withUnlockedAccountByPosition(Arrays.asList(0, 1));
	}


	@Test
	public void simpleTestWithClientCreation() throws IOException {
		Web3j web3j = ganacheContainer.getWeb3j();
		assertNotNull(web3j);
		assertEquals(web3j.ethBlockNumber().send().getBlockNumber(), BigInteger.ZERO);
		assertNotNull(ganacheContainer.getCredentials());
		assertEquals(ganacheContainer.getCredentials().size(), 2);
	}


	@Test
	public void testAccountCreation() throws IOException {
		ganacheContainer.stop();
		ganacheContainer = new GanacheContainer()
				.withAccounts(
						Arrays.asList(
								Account.builder().privateKey(PRIVATE_KEY_0).balance(BigInteger.ONE).build(),
								Account.builder().privateKey(PRIVATE_KEY_1).balance(BigInteger.TEN).build()
						)
				);
		ganacheContainer.start();
		Web3j web3j = ganacheContainer.getWeb3j();
		assertEquals(web3j.ethBlockNumber().send().getBlockNumber(), BigInteger.ZERO);
		assertEquals(ganacheContainer.getCredentials().size(), 2);
		assertEquals(web3j.ethGetBalance(((Credentials) ganacheContainer.getCredentials().get(0)).getAddress(), DefaultBlockParameter.valueOf("latest")).send().getBalance(),
				BigInteger.ONE);

		assertEquals(web3j.ethGetBalance(((Credentials) ganacheContainer.getCredentials().get(1)).getAddress(), DefaultBlockParameter.valueOf("latest")).send().getBalance(),
				BigInteger.TEN);



	}

	@Test(expected = Exception.class)
	public void testGetClientFail(){
		ganacheContainer.stop();
		ganacheContainer.getWeb3j();
	}

}
