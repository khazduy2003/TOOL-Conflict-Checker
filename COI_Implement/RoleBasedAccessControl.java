package COI_Implement;
import org.w3c.dom.ls.LSOutput;

import java.util.*;


enum role {
    TELLER, LOAN_OFFICER, AUDITOR
}

class User {

    private String username;
    private COI_Implement.role role;
    private Set<String> permissions;
    private Set<String> roleConflicts;
    private String location; // Thông tin vị trí
    private String creationLocation;

    public User(String username, COI_Implement.role role, Set<String> permissions, Set<String> roleConflicts, String location) {
        this.username = username;
        this.role = role;
        this.permissions = permissions;
        this.roleConflicts = roleConflicts;
        this.location = location;  // Gán vị trí khi tạo đối tượng
    }
    private String getCurrentLocation() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTrace) {
            if (element.getClassName().equals(RoleBasedAccessControl.class.getName())) {
                return element.getFileName() + ": dòng " + element.getLineNumber();  // Đảm bảo lấy chính xác dòng trong RoleBasedAccessControl
            }
        }
        return "Không xác định vị trí";  // Nếu không tìm thấy dòng
    }

    public String getLocation() {
        return location;
    }
    public String getUsername() {
        return username;
    }

    public COI_Implement.role getRole() {
        return role;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public Set<String> getRoleConflicts() {
        return roleConflicts;
    }

}

class AccessControl {
    private final Map<role, Set<String>> rolePermissions = new HashMap<>();

    public AccessControl() {
        rolePermissions.put(role.TELLER, new HashSet<>());
        rolePermissions.put(role.LOAN_OFFICER, new HashSet<>());
        rolePermissions.put(role.AUDITOR, new HashSet<>());

        // Define permissions
        rolePermissions.get(role.TELLER).add("PROCESS_CASH");//test
        rolePermissions.get(role.TELLER).add("VIEW_ACCOUNTS");//test

        rolePermissions.get(role.LOAN_OFFICER).add("CREATE_LOAN");
        //rolePermissions.get(role.LOAN_OFFICER).add("APPROVE_LOAN"); // Đây là xung đột lợi ích

        rolePermissions.get(role.AUDITOR).add("AUDIT_TRANSACTIONSSS");
    }

    public Set<String> getPermissionsByRole(role role) {
        return rolePermissions.get(role);
    }
}

class RoleConflicts {

    private final Map<role, Set<String>> roleConflicts = new HashMap<>();
    public  RoleConflicts() {

        roleConflicts.put(role.TELLER, new HashSet<>());
        roleConflicts.put(role.LOAN_OFFICER, new HashSet<>());
        roleConflicts.put(role.AUDITOR, new HashSet<>());

        roleConflicts.get(role.TELLER).add("CREATE_LOAN");
        roleConflicts.get(role.TELLER).add("AUDIT_TRANSACTIONS");
        roleConflicts.get(role.LOAN_OFFICER).add("AUDIT_TRANSACTIONS");
        roleConflicts.get(role.AUDITOR).add("PROCESS_CASH");
    }

    public Set<String> getRoleConflicts(role role) {
        return roleConflicts.get(role);
    }
}

public class RoleBasedAccessControl {
        AccessControl ac = new AccessControl();
        RoleConflicts rc = new RoleConflicts();
    String getCurrentLocation() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
//        System.out.println(stackTrace.length);
//        for (int i =0 ;i<stackTrace.length;i++) {
//            System.out.println(stackTrace[i]);
//        }
        if (stackTrace.length >= 3) {
            // Lấy phần tử thứ 2 (index 2) trong stack trace (dòng gán User)
            StackTraceElement caller = stackTrace[2];
            return caller.getFileName() + ": dòng " + caller.getLineNumber();
        }
        return "Không xác định vị trí";
    }


        User loanOfficer = new User("loan_officer", role.LOAN_OFFICER, ac.getPermissionsByRole(role.LOAN_OFFICER), rc.getRoleConflicts(role.LOAN_OFFICER), getCurrentLocation());
        User CONG = new User("CONG", role.TELLER, ac.getPermissionsByRole(role.TELLER), rc.getRoleConflicts(role.TELLER),getCurrentLocation());
        User Huong = new User("Huong", role.AUDITOR, ac.getPermissionsByRole(role.AUDITOR), rc.getRoleConflicts(role.AUDITOR),getCurrentLocation());
        Map<Integer, User> MapUsers = new LinkedHashMap<>();
        static Integer num = 0;
        RoleBasedAccessControl() {
            MapUsers.put(num++,CONG);
            MapUsers.put(num++,loanOfficer);
            MapUsers.put(num++,Huong);
        }

}