package criminaldb.model;

public class Criminal {
    private int    criminalId;
    private String name;
    private int    age;
    private String gender;
    private String address;
    private String nationality;
    private int    heightCm;
    private int    weightKg;
    private String bloodGroup;
    private String photoPath;
    private boolean isWanted;

    public Criminal() {}

    public Criminal(int criminalId, String name, int age, String gender,
                    String address, String nationality, int heightCm,
                    int weightKg, String bloodGroup, String photoPath, boolean isWanted) {
        this.criminalId  = criminalId;
        this.name        = name;
        this.age         = age;
        this.gender      = gender;
        this.address     = address;
        this.nationality = nationality;
        this.heightCm    = heightCm;
        this.weightKg    = weightKg;
        this.bloodGroup  = bloodGroup;
        this.photoPath   = photoPath;
        this.isWanted    = isWanted;
    }

    // Getters & Setters
    public int     getCriminalId()  { return criminalId;  }
    public void    setCriminalId(int v)  { criminalId = v; }
    public String  getName()        { return name;        }
    public void    setName(String v)     { name = v;       }
    public int     getAge()         { return age;         }
    public void    setAge(int v)         { age = v;        }
    public String  getGender()      { return gender;      }
    public void    setGender(String v)   { gender = v;     }
    public String  getAddress()     { return address;     }
    public void    setAddress(String v)  { address = v;    }
    public String  getNationality() { return nationality; }
    public void    setNationality(String v) { nationality = v; }
    public int     getHeightCm()    { return heightCm;    }
    public void    setHeightCm(int v)    { heightCm = v;   }
    public int     getWeightKg()    { return weightKg;    }
    public void    setWeightKg(int v)    { weightKg = v;   }
    public String  getBloodGroup()  { return bloodGroup;  }
    public void    setBloodGroup(String v) { bloodGroup = v; }
    public String  getPhotoPath()   { return photoPath;   }
    public void    setPhotoPath(String v)  { photoPath = v;  }
    public boolean isWanted()       { return isWanted;    }
    public void    setWanted(boolean v)    { isWanted = v;   }

    @Override
    public String toString() { return name; }
}
