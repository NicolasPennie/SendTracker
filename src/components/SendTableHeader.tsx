import React from 'react';
import {
  TableCell,
  TableHead,
  TableRow,
  TableSortLabel,
} from '@material-ui/core';

import { SortOrder } from '../utility/sort';
import { Header, HeaderId } from '../models/header';

export interface SendTableHeaderProps {
  order: SortOrder;
  orderBy: HeaderId;
  onRequestSort: (id: HeaderId) => void;
}

export default function SendTableHeader(props: SendTableHeaderProps) {
  const { order, orderBy, onRequestSort } = props;

  const headers: Array<Header> = [
    { id: 'name', label: 'Name' },
    { id: 'style', label: 'Style' },
    { id: 'grade', label: 'Grade' },
    { id: 'tickType', label: 'Tick Type' },
    { id: 'location', label: 'Location' },
  ];

  return (
    <TableHead>
      <TableRow>
        {headers.map((header, index) => (
          <TableCell
            key={header.id}
            align={index === 0 ? 'left' : 'right'}
            sortDirection={orderBy === header.id ? order : false}
          >
            <TableSortLabel
              active={orderBy === header.id}
              direction={order}
              onClick={() => onRequestSort(header.id)}
            >
              {header.label}
            </TableSortLabel>
          </TableCell>
        ))}
      </TableRow>
    </TableHead>
  );
}

