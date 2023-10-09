package com.example.springtest.extension;

import com.example.springtest.mapper.AddressMapper;
import com.example.springtest.mapper.CustomerMapper;
import com.example.springtest.services.CustomerServiceTest;
import java.util.List;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mapstruct.factory.Mappers;

public class MapStructsResolver implements BeforeEachCallback {

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    List<Object> testInstances = context.getRequiredTestInstances().getAllInstances();
    ((CustomerServiceTest) testInstances.get(0))
        .setMapper(Mappers.getMapper(CustomerMapper.class));

    ((CustomerServiceTest) testInstances.get(0))
        .setAddressMapper(Mappers.getMapper(AddressMapper.class));
  }
}
