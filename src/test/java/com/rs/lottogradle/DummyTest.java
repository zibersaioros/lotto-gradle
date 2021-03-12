package com.rs.lottogradle;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.IntStream;

public class DummyTest {

    @Test
    public void test(){
        IntStream intStream = IntStream.of(531
                , 307
                , 381
                , 548
                , 448
                , 615
                , 291
                , 337
                , 306
                , 641
                , 526
                , 317
                , 356
                , 493
                , 985
                , 459
                , 296
                , 306
                , 500
                , 174
                , 223
                , 254
                , 197
                , 260
                , 208
                , 276
                , 328
                , 355
                , 358
                , 270
                , 370
                , 230
                , 300
                , 254
                , 420
                , 232
                , 566
                , 353
                , 229
                , 410
                , 613
                , 260
                , 537
                , 315
                , 361
                , 824
                , 704
                , 359
                , 703
                , 175
                , 323
                , 364
                , 211
                , 315
                , 344);

//        System.out.println(intStream.count());

        OptionalDouble average = intStream.average();
        System.out.println(average.getAsDouble());
    }
}
