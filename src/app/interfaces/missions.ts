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
  transactions: Transaction[];
}

export interface Budget {
  id: number;
  mission:Missions
  amount: number,
  reason: string,
  rejectionreason: string,
  status: string,
  timestamp: any
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
  phonenumber?: string; // Optional
  personalinformation: PersonalInformation;
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
  firstnameen: string;
  lastnameen: string;
  firstnamear?: string | null; // Optional
  lastnamear?: string | null; // Optional
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

export interface PaginatedBudgets {
  elements: Budget[];
  page: number;
  pages : number;
  size:  number;
  nb_elements:  number;
}

export interface PaginatedTransaction {
  elements: Transaction[];
  page: number;
  pages : number;
  size:  number;
  nb_elements:  number;
}


interface Transaction {
  id: number;
  status: any; // Adjust as needed
  name: string;
  description: string;
  rejectionReason: string | null;
  amount: number;
  archive: boolean;
  timestamp: number; // You might want to use Date if you prefer
  missiontitle: string;
  missionId: number;
  createdbyid: number;
}
