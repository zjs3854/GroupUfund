export interface Need {
    id: number;
    name: string;
    description: string;
    quantity: number;
    price: number;
    notw: boolean;
    expireDate: Date|null;
  }