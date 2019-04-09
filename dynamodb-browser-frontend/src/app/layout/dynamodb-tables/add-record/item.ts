export class Item {
    table_name: string;
    attributes: Attribute[];
}

export class Attribute {
    key: string;
    attribute_name: string;
    data_type: string;
    attribute_value: string;
}
