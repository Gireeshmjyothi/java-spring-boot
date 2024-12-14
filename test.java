package com.continuum.cms.mapper;

import com.continuum.cms.entity.ChargingSession;
import com.continuum.cms.model.response.ChargingSessionResponse;
import com.continuum.cms.util.DateUtil;
import com.continuum.cms.util.EncryptionUtil;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ChargingSessionMapper {
    private final MapperFactory mapperFactory;
    private final MapperFacade mapperFacade;
    @Autowired
    public ChargingSessionMapper() {
        this.mapperFactory = new DefaultMapperFactory.Builder().build();
        this.mapperFacade = mapperFactory.getMapperFacade();
        configureMapper();
    }

    private void configureMapper() {
        mapperFactory.classMap(ChargingSession.class,ChargingSessionResponse.class)
                .field("chargingStation.locationId", "locationId")
                .field("chargingStation.name", "name")
                .field("chargingStation.city", "city")
                .field("latitude", "geoLocation.latitude")
                .field("longitude", "geoLocation.longitude")
                .field("transactionId", "sessionId")
                .field("vendorCustomer.firstName", "userDetails.firstName")
                .field("vendorCustomer.lastName", "userDetails.lastName")
                .field("vendorCustomer.userType", "userDetails.userType")
                .field("vendorBankDetails.vendor.name", "vendorName")
                .field("vendorBankDetails.vendor.id", "vendorId")
                .field("vendorBankDetails.cin", "vendorCode")
                .field("carbonCreditDetail.creditScorePoints", "carbonCreditGenerated")
                .field("carbonCreditDetail.certificateNumber", "batchNo")
                .customize(new CustomMapper<ChargingSession, ChargingSessionResponse>() {
                    @Override
                    public void mapAtoB(ChargingSession source, ChargingSessionResponse target, MappingContext context) {
                        if (source.getVendorCustomer().getEmail() != null) {
                            target.getUserDetails().setEmail(EncryptionUtil.decryptEmailAndMobile(source.getVendorCustomer().getEmail()));
                        }
                        if (source.getVendorCustomer().getMobile() != null) {
                            target.getUserDetails().setMobile(EncryptionUtil.decryptEmailAndMobile(source.getVendorCustomer().getMobile()));
                        }
                        if(source.getStartAt() != null && source.getStopAt() != null){
                            target.setStartAt(DateUtil.getISTToUTC(source.getStartAt()));
                            target.setStopAt(DateUtil.getISTToUTC(source.getStopAt()));
                        }
                    }
                })
                .byDefault()
                .mapNulls(false)
                .register();
    }

    public List<ChargingSessionResponse> mapToChargingSessionList(List<ChargingSession> chargingSessionList) {
        log.info("Mapping Charging-session list.");
        return mapperFacade.mapAsList(chargingSessionList, ChargingSessionResponse.class);
    }

}


 Page<ChargingSession> chargingSessionsList = chargingSessionDao.getAllChargingSession(chargingSessionFilter, search, pageable, vendorCodes);
        log.info("Successfully fetched charging session details.");
        return ResponseDto.<ChargingSessionResponse>builder()
                .data(sessionMapper.mapToChargingSessionList(chargingSessionsList.getContent()))
                .status(RESPONSE_SUCCESS)
                .count(chargingSessionsList.stream().count())
                .total(chargingSessionsList.getTotalElements())
                .build();
    }

// implementation 'com.github.jmapper-framework:orika:1.7.5'

// implementation 'ma.glasnost.orika:orika-core:1.5.4'
