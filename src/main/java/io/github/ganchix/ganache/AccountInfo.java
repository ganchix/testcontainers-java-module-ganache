package io.github.ganchix.ganache;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.web3j.crypto.Credentials;

/**
 * Created by Rafael Ríos on 20/05/18.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountInfo {

    String address;
    String privateKey;
    Credentials credential;
}
