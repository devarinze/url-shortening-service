package com.tinyurl.core.data;

import javax.persistence.PrePersist;
import java.util.Date;

public class BaseEntityListener {

    @PrePersist
    public void touchForCreate(BaseEntity data) {
        Date createDate = new Date();
        data.setCreateDate(createDate);
//        AuditDetails auditDetails = getAuditDetails();
//        if(ObjectUtils.isEmpty(data.getCreatedBy())){
//            data.setCreatedBy(auditDetails.getCreatedBy());
//        }
//        if(ObjectUtils.isEmpty(data.getCmpCode())){
//            data.setCmpCode(auditDetails.getCmpCode());
//        }
//        if(ObjectUtils.isEmpty(data.getParentCmpCode())){
//            data.setParentCmpCode(auditDetails.getParentCmpCode());
//        }
    }

//    private AuditDetails getAuditDetails() {
//        AuditorAware<AuditDetails> auditor = BeanUtil.getBean(AuditorAware.class);
//        AuditDetails auditDetails = auditor.getCurrentAuditor().get();
//        return auditDetails;
//    }
}
