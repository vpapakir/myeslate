package gr.cti.eslate.mapModel;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

class BigLittleDataInputStream extends DataInputStream {
    BigLittleDataInputStream(InputStream in) {
        super(in);
    }

    int readLittleInt() throws IOException {
        int i[]=new int[4];
        byte b[]=new byte[4];

        try {
        readFully(b);
        if (b[3]<0) i[0]=b[3]+256;
        else i[0]=b[3];
        if (b[2]<0) i[1]=b[2]+256;
        else i[1]=b[2];
        if (b[1]<0) i[2]=b[1]+256;
        else i[2]=b[1];
        if (b[0]<0) i[3]=b[0]+256;
        else i[3]=b[0];
		} catch(IOException ee) {
        	ee.printStackTrace();
            throw ee;
        }

        return ((i[0]<<24)+(i[1]<<16)+(i[2]<<8)+i[3]);
    }

    int readBigInt() throws IOException {
        int i[]=new int[4];
        byte b[]=new byte[4];

        readFully(b);
        if (b[0]<0) i[0]=b[0]+256;
        else i[0]=b[0];
        if (b[1]<0) i[1]=b[1]+256;
        else i[1]=b[1];
        if (b[2]<0) i[2]=b[2]+256;
        else i[2]=b[2];
        if (b[3]<0) i[3]=b[3]+256;
        else i[3]=b[3];

        return ((i[0]<<24)+(i[1]<<16)+(i[2]<<8)+i[3]);
    }


    double readLittleDouble() throws IOException {
        byte b[]=new byte[8];
        byte sign=1;
        int exponent=0;
        int temp,k;
        long i[]=new long[7];
        double ret;
// Elegxoi 0, inf

        readFully(b);
//        b[0]=bu[s]; b[1]=bu[pos+1]; b[2]=bu[pos+2]; b[3]=bu[pos+3];
//        b[4]=bu[pos+4]; b[5]=bu[pos+5]; b[6]=bu[pos+6]; b[7]=bu[pos+7];

        if (b[7]<0) sign=-1;

        exponent=(b[7] & 0x7F)<<4;
        if (b[6]<0) temp=b[6]+256;
        else temp=b[6];
        if (temp>=128) {exponent+=8; temp-=128;}
        if (temp>=64) {exponent+=4; temp-=64;}
        if (temp>=32) {exponent+=2; temp-=32;}
        if (temp>=16) exponent+=1;

        i[6]=(b[6] & 0x0F) + /*implicit 1*/ 16;
        for (k=5;k>-1;k--)
            if (b[k]<0) i[k]=b[k]+256;
            else i[k]=b[k];
        ret=((i[6]<<48)+(i[5]<<40)+(i[4]<<32)+(i[3]<<24)+(i[2]<<16)+(i[1]<<8)+i[0])*Math.pow(2d,(exponent-1023)-52);

        return sign*ret;
    }

    double readBigDouble() throws IOException {
        return readDouble();
    }

}
