import dayjs from 'dayjs/esm';

export interface IEvent {
  id?: number;
  eventTitle?: string;
  eventDate?: dayjs.Dayjs;
  description?: string;
}

export class Event implements IEvent {
  constructor(public id?: number, public eventTitle?: string, public eventDate?: dayjs.Dayjs, public description?: string) {}
}

export function getEventIdentifier(event: IEvent): number | undefined {
  return event.id;
}
