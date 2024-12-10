@Component
@RequiredArgsConstructor
public class TokenValidator extends BaseValidator {

    public void validateAccessTokenRequest(String merchantApiKeyId, String merchantApiKeySecret) {
        checkMandatoryField("Merchant Api Key Id", merchantApiKeyId);
        checkMandatoryField("Merchant Api Key Secret", merchantApiKeySecret);
        throwIfErrors();
    }

}
