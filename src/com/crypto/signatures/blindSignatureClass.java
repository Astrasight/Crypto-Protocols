package com.crypto.signatures;

import java.math.BigInteger;

public class blindSignatureClass {

    public static BigInteger blindHideMsg(BigInteger msg, BigInteger factor, BigInteger e, BigInteger n) {
        BigInteger hideMsg = msg.multiply(factor.modPow(e, n)).mod(n); // M со штрихом
        return hideMsg;
    }

    public static BigInteger blindSignature(BigInteger blindMsg, BigInteger d, BigInteger n) {
        BigInteger blindSig = blindMsg.modPow(d, n); // сторона B подписывает M со штрихом
        return blindSig;
    }

    public static BigInteger blindRetrieveSig(BigInteger blindSig, BigInteger factor, BigInteger n) {
        BigInteger signature = blindSig.multiply(factor.modInverse(n)).mod(n); // формирование подписи пользователя
        return signature;                                                      // B к сообщению M
    }
}
