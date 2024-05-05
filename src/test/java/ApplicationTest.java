import com.solution.model.AddUserReq;
import com.solution.utils.JackJsonUtil;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

public class ApplicationTest extends SpringMvcBase {

    @Test
    public void TestNoAuth() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/admin/addUser")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(result -> {
                    System.out.println(result.getResponse().getContentAsString());
                })
                .andExpect(MockMvcResultMatchers.status().is(200))
        ;
    }

    @Test
    public void TestAddUser() throws Exception {
        AddUserReq addUserReq = new AddUserReq();
        addUserReq.setUserId(123);
        List<String> list = new ArrayList<String>();
        list.add("A");
        list.add("B");
        list.add("C");
        addUserReq.setEndpoint(list);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/admin/addUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("userId", "123")
                        .header("accountName", "xxx")
                        .header("role", "admin")
                        .content(JackJsonUtil.getJson(addUserReq)))
                .andDo(result -> {
                    System.out.println(result.getResponse().getContentAsString());
                })
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$").value("success"))
        ;

        mockMvc.perform(
                MockMvcRequestBuilders.get("/admin/addUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("userId", "123")
                        .header("accountName", "xxx")
                        .header("role", "user")
                        .content(JackJsonUtil.getJson(addUserReq)))
                .andDo(result -> {
                    System.out.println(result.getResponse().getContentAsString());
                })
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$").value("no access to this endpoint"))
        ;
    }

    @Test
    public void TestResourceCheck() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/user/A")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .header("userId", "123")
                        .header("accountName", "xxx")
                        .header("role", "admin"))
                .andDo(result -> {
                    System.out.println(result.getResponse().getContentAsString());
                })
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$").value("have this access"))
        ;

        mockMvc.perform(
                MockMvcRequestBuilders.get("/user/D")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .header("userId", "123")
                        .header("accountName", "xxx")
                        .header("role", "admin"))
                .andDo(result -> {
                    System.out.println(result.getResponse().getContentAsString());
                })
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$").value("not have this access"))
        ;
    }
}
