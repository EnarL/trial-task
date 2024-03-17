package com.fujitsu.trialtask.Components;
import com.fujitsu.trialtask.Entities.*;
import com.fujitsu.trialtask.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

@Component
public class EFInitializer implements ApplicationRunner {
    private final AtefRepository atefRepository;
    private final WsefRepository wsefRepository;
    private final WpefRepository wpefRepository;

    @Autowired
    public EFInitializer(AtefRepository atefRepository, WsefRepository wsefRepository, WpefRepository wpefRepository) {
        this.atefRepository = atefRepository;
        this.wsefRepository = wsefRepository;
        this.wpefRepository = wpefRepository;
    }

  //adding base values to db
    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Atef> atefList = Arrays.asList(
                new Atef("-10-0", 0.5),
                new Atef("<-10", 1)
        );
        atefRepository.saveAll(atefList);

        List<Wsef> wsefList = Arrays.asList(
                new Wsef(">20", -1),
                new Wsef("10-20", 0.5)

        );
        wsefRepository.saveAll(wsefList);

        List<Wpef> wpefList = Arrays.asList(
                new Wpef("snow", 1),
                new Wpef("sleet", 1),
                new Wpef("rain", 0.5),
                new Wpef("glaze", -1),
                new Wpef("thunder", -1),
                new Wpef("hail", -1)
        );
        wpefRepository.saveAll(wpefList);
    }
    }