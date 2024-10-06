class TodoList {
    constructor(private _id: number,
                private _title: string,
                private _numItems: number,
                private _numCompleted: number,
                private _created: string,
                private _updated: string) {}


    get id(): number {
        return this._id;
    }

    set id(value: number) {
        this._id = value;
    }

    get title(): string {
        return this._title;
    }

    set title(value: string) {
        this._title = value;
    }

    get numItems(): number {
        return this._numItems;
    }

    set numItems(value: number) {
        this._numItems = value;
    }

    get numCompleted(): number {
        return this._numCompleted;
    }

    set numCompleted(value: number) {
        this._numCompleted = value;
    }

    get created(): string {
        return this._created;
    }

    set created(value: string) {
        this._created = value;
    }

    get updated(): string {
        return this._updated;
    }

    set updated(value: string) {
        this._updated = value;
    }
}
