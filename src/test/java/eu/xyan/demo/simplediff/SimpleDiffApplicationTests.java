package eu.xyan.demo.simplediff;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.xyan.demo.simplediff.algorithm.implementation.dto.DiffDtoV1;
import eu.xyan.demo.simplediff.algorithm.implementation.dto.InsightDtoV1;
import eu.xyan.demo.simplediff.algorithm.implementation.enumeration.EqualityStatus;
import eu.xyan.demo.simplediff.repository.DataPairRepository;
import eu.xyan.demo.simplediff.service.DataPairService;
import eu.xyan.demo.simplediff.service.dto.DataDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@TestPropertySource(
        locations = "classpath:application-test.yml")
@SpringBootTest
@WebAppConfiguration
public class SimpleDiffApplicationTests {

    @Autowired
    private DataPairRepository dataPairRepository;

    @Autowired
    private DataPairService dataPairService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        dataPairRepository.deleteAll();
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void nonExistingIdDiff() throws Exception {
        mockMvc.perform(get("/v1/diff/1")).andExpect(status().isNotFound());
    }


    @Test
    public void saveLeftData() throws Exception {
        String leftPart = getBase64Data("test left part");

        DataDto dataDto = new DataDto();
        dataDto.setData(leftPart);

        this.mockMvc.perform(post("/v1/diff/1/left")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(dataDto))).andExpect(status().isOk())
                .andExpect(content().json("{'id':1,'leftData':'" + leftPart + "', 'rightData':null}"));
    }

    @Test
    public void saveRightData() throws Exception {
        String rightPart = getBase64Data("test right part");

        DataDto dataDto = new DataDto();
        dataDto.setData(rightPart);

        this.mockMvc.perform(post("/v1/diff/1/right")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(dataDto))).andExpect(status().isOk())
                .andExpect(content().json("{'id':1,'rightData':'" + rightPart + "', 'leftData':null}"));
    }

    private void saveBothEqualData() throws Exception {
        String leftPart = getBase64Data("test data");
        String rightPart = getBase64Data("test data");

        // Left
        DataDto dataDto = new DataDto();
        dataDto.setData(leftPart);

        this.mockMvc.perform(post("/v1/diff/1/left")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(dataDto))).andExpect(status().isOk())
                .andExpect(content().json("{'id':1,'leftData':'" + leftPart + "', 'rightData':null}"));

        // Right
        dataDto = new DataDto();
        dataDto.setData(rightPart);

        this.mockMvc.perform(post("/v1/diff/1/right")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(dataDto))).andExpect(status().isOk())
                .andExpect(content().json("{'id':1,'leftData':'" + leftPart + "', 'rightData':'" + rightPart + "'}"));
    }

    @Test
    public void testEqualData() throws Exception {
        saveBothEqualData();   // test data

        DiffDtoV1 diffDtoV1 = DiffDtoV1.builder().equalityStatus(EqualityStatus.EQUAL).build();

        // Equal
        this.mockMvc.perform(get("/v1/diff/1")).andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(diffDtoV1)));
    }

    @Test
    public void testNotEqualSizeData() throws Exception {
        saveBothEqualData();    // test data

        String rightPart = getBase64Data("test right part");

        DataDto dataDto = new DataDto();
        dataDto.setData(rightPart);

        // Set different right data
        this.mockMvc.perform(post("/v1/diff/1/right")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(dataDto))).andExpect(status().isOk());

        DiffDtoV1 diffDtoV1 = DiffDtoV1.builder().equalityStatus(EqualityStatus.NOT_EQUAL_SIZE).build();

        // Not Equal Size
        this.mockMvc.perform(get("/v1/diff/1")).andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(diffDtoV1)));
    }

    @Test
    public void testNotEqualData() throws Exception {
        saveBothEqualData();    // test data

        String rightPart = getBase64Data("rest datE");

        DataDto dataDto = new DataDto();
        dataDto.setData(rightPart);

        // Set different right data
        this.mockMvc.perform(post("/v1/diff/1/right")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(dataDto))).andExpect(status().isOk());

        DiffDtoV1 diffDtoV1 = DiffDtoV1.builder().equalityStatus(EqualityStatus.NOT_EQUAL)
                .insight(new InsightDtoV1(1L, 1L))
                .insight(new InsightDtoV1(9L, 1L))
                .build();

        // Not Equal
        this.mockMvc.perform(get("/v1/diff/1")).andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(diffDtoV1)));
    }

    private String getBase64Data(String data) {
        return Base64.getEncoder().encodeToString(data.getBytes());
    }

}
