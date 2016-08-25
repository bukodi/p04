package com.bukodi.p04.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.bukodi.p04.domain.enumeration.Planets;

/**
 * A Foo.
 */
@Entity
@Table(name = "foo")
public class Foo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "optional_string")
    private String optionalString;

    @NotNull
    @Column(name = "required_string", nullable = false)
    private String requiredString;

    @Column(name = "zoned_date_time")
    private ZonedDateTime zonedDateTime;

    @Column(name = "logical")
    private Boolean logical;

    @Lob
    @Column(name = "blob_field")
    private byte[] blobField;

    @Column(name = "blob_field_content_type")
    private String blobFieldContentType;

    @Lob
    @Column(name = "image_blob")
    private byte[] imageBlob;

    @Column(name = "image_blob_content_type")
    private String imageBlobContentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "enum_field")
    private Planets enumField;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOptionalString() {
        return optionalString;
    }

    public Foo optionalString(String optionalString) {
        this.optionalString = optionalString;
        return this;
    }

    public void setOptionalString(String optionalString) {
        this.optionalString = optionalString;
    }

    public String getRequiredString() {
        return requiredString;
    }

    public Foo requiredString(String requiredString) {
        this.requiredString = requiredString;
        return this;
    }

    public void setRequiredString(String requiredString) {
        this.requiredString = requiredString;
    }

    public ZonedDateTime getZonedDateTime() {
        return zonedDateTime;
    }

    public Foo zonedDateTime(ZonedDateTime zonedDateTime) {
        this.zonedDateTime = zonedDateTime;
        return this;
    }

    public void setZonedDateTime(ZonedDateTime zonedDateTime) {
        this.zonedDateTime = zonedDateTime;
    }

    public Boolean isLogical() {
        return logical;
    }

    public Foo logical(Boolean logical) {
        this.logical = logical;
        return this;
    }

    public void setLogical(Boolean logical) {
        this.logical = logical;
    }

    public byte[] getBlobField() {
        return blobField;
    }

    public Foo blobField(byte[] blobField) {
        this.blobField = blobField;
        return this;
    }

    public void setBlobField(byte[] blobField) {
        this.blobField = blobField;
    }

    public String getBlobFieldContentType() {
        return blobFieldContentType;
    }

    public Foo blobFieldContentType(String blobFieldContentType) {
        this.blobFieldContentType = blobFieldContentType;
        return this;
    }

    public void setBlobFieldContentType(String blobFieldContentType) {
        this.blobFieldContentType = blobFieldContentType;
    }

    public byte[] getImageBlob() {
        return imageBlob;
    }

    public Foo imageBlob(byte[] imageBlob) {
        this.imageBlob = imageBlob;
        return this;
    }

    public void setImageBlob(byte[] imageBlob) {
        this.imageBlob = imageBlob;
    }

    public String getImageBlobContentType() {
        return imageBlobContentType;
    }

    public Foo imageBlobContentType(String imageBlobContentType) {
        this.imageBlobContentType = imageBlobContentType;
        return this;
    }

    public void setImageBlobContentType(String imageBlobContentType) {
        this.imageBlobContentType = imageBlobContentType;
    }

    public Planets getEnumField() {
        return enumField;
    }

    public Foo enumField(Planets enumField) {
        this.enumField = enumField;
        return this;
    }

    public void setEnumField(Planets enumField) {
        this.enumField = enumField;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Foo foo = (Foo) o;
        if(foo.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, foo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Foo{" +
            "id=" + id +
            ", optionalString='" + optionalString + "'" +
            ", requiredString='" + requiredString + "'" +
            ", zonedDateTime='" + zonedDateTime + "'" +
            ", logical='" + logical + "'" +
            ", blobField='" + blobField + "'" +
            ", blobFieldContentType='" + blobFieldContentType + "'" +
            ", imageBlob='" + imageBlob + "'" +
            ", imageBlobContentType='" + imageBlobContentType + "'" +
            ", enumField='" + enumField + "'" +
            '}';
    }
}
