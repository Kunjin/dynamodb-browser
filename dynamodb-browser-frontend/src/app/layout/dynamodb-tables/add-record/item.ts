export class Item {
    table_name: string;
    hash_key: Attribute[];
    range_key: Attribute[];
    attributes: Attribute[];
}

export class Attribute {
    attribute_name: string;
    data_type: string;
    attribute_value: string;
}
