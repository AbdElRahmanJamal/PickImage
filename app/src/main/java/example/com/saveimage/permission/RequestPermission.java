package example.com.saveimage.permission;

public class RequestPermission {
    IPermission permission;

    public RequestPermission(IPermission permission) {
        this.permission = permission;
        permission.showPreview();
    }
}
