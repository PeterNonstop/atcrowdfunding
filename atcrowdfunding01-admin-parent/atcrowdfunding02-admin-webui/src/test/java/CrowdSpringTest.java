import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.mapper.AdminMapper;
import com.atguigu.crowd.mapper.RoleMapper;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.service.api.RoleService;
import com.atguigu.crowd.util.CrowdUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

// 指定 Spring 给 Junit 提供的运行器类
@RunWith(SpringJUnit4ClassRunner.class)
//加载spring配置文件
@ContextConfiguration(locations = {"classpath:spring-persist-mybatis.xml","classpath:spring-persist-tx.xml"})
public class CrowdSpringTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    //批量执行
    @Autowired
    SqlSession sqlSession;

    @Test
    public void testTx(){
        Admin admin = new Admin(null, "hello", "123456", "爱我中华", "123@qq.com", null);
        adminService.saveAdmin(admin);
    }

    @Test
    public void testDataSource() throws SQLException {
        // 1.通过数据源对象获取数据源连接
        Connection connection = dataSource.getConnection();
        // 2.打印数据库连接
        System.out.println(connection);
    }

    @Test
    public void testInsertAdmin() {
        Admin admin = new Admin(null, "dog", "123456", "爱我中华", "123@qq.com", null);
        int i = adminMapper.insert(admin);
        System.out.println("受影响的行数：" + i);

    }

    @Test
    public void testMd5(){
        String source = "123123";
        String md5 = CrowdUtil.md5(source);
        System.out.println("密文："+md5);
    }

    @Test
    public void testInsertAdminBatch() {
        //批量插入员工，使用可以批量操作的sqlSession
        AdminMapper mapper = sqlSession.getMapper(AdminMapper.class);
        for (int i = 0; i < 1000; i++) {
            String uid = UUID.randomUUID().toString().substring(0, 5) + i;
            String md5 = CrowdUtil.md5(uid);
            mapper.insertSelective(new Admin(null, uid, md5, uid,uid + "@test.com", null));
        }
        System.out.println("插入完成====");

    }

    @Test
    public void testInsertRoleBatch() {
        //批量插入员工，使用可以批量操作的sqlSession
        RoleMapper mapper = sqlSession.getMapper(RoleMapper.class);
        for (int i = 0; i < 1000; i++) {
            String uid = UUID.randomUUID().toString().substring(0, 5) + i;
            String md5 = CrowdUtil.md5(uid);
            mapper.insertSelective(new Role(null, uid));
        }
        System.out.println("插入完成====");

    }
}
