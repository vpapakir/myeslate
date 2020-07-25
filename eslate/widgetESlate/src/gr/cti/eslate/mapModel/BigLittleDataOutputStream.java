package gr.cti.eslate.MapModel;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

class BigLittleDataOutputStream extends DataOutputStream {
    BigLittleDataOutputStream(OutputStream in) {
        super(in);
    }

    void writeLittleDouble(double d) throws Exception {
        long bd=Double.doubleToLongBits(d);
        byte[] b=new byte[8];

        for (int i=0;i<8;i++) {
            b[i]=(byte) (bd & 0xFF);
            bd=bd>>8;
        }

        write(b);
    }

    void writeBigDouble(double j) throws IOException {
        writeDouble(j);
    }

    void writeLittleInt(int j) throws IOException {
        byte[] b=new byte[4];
        for (int i=0;i<4;i++) {
            b[i]=(byte) (j & 0xFF);
            j=j>>8;
        }

        write(b);
    }

    void writeBigInt(int j) throws IOException {
        writeInt(j);
    }
}
