export interface Missions {
  address: string;
  archive: boolean;
  budget: number;
  enddate: number; // Timestamp
  id: number;
  location: Location;
  participants: Participant[];
  startdate: number; // Timestamp
  status: string;
  teamLead: Participant;
  active: boolean;
  deleted: boolean;
  title: string;
}

export interface Location {
  id: number;
  latitude: number;
  longitude: number;
  name: string;
  timestamp: number; // Timestamp
}

export interface Participant {
  id: number;
  username: string;
  email: string;
  name: string;
  phoneNumber?: string; // Optional
  personalInformation: PersonalInformation;
  privileges: any[]; // Define specific types as needed
  profileImage?: string | null; // Optional
  provider: string;
  role: Role;
  suspendReason: string;
  verified: boolean;
  lastOtp?: string | null; // Optional
  timestamp: number; // Timestamp
}

export interface PersonalInformation {
  id: number;
  firstNameEn: string;
  lastNameEn: string;
  firstNameAr?: string | null; // Optional
  lastNameAr?: string | null; // Optional
}

export interface Role {
  id: number;
  role: string;
}


export interface PaginatedMissions {
  elements: Missions[];
  page: number;
  pages : number;
  size:  number;
  nb_elements:  number;
}
