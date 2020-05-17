import React from 'react';
import {
  Table,
  TableBody,
  TableCell,
  TableRow,
  TablePagination,
} from '@material-ui/core';
import SendTableHeader from './SendTableHeader';
import { stableSort, SortOrder, getSorting } from '../utility/sort';
import { Send } from '../models/send';
import { HeaderId } from '../models/header';

export interface SendTableProps {
  sends: Send[];
}

export default function SendTable(props: SendTableProps) {
  const { sends } = props;
  const [order, setOrder] = React.useState<SortOrder>('asc');
  const [orderBy, setOrderBy] = React.useState<HeaderId>('name');
  const [page, setPage] = React.useState(0);

  const ROWS_PER_PAGE = 10;

  const handleSortRequest = (property: HeaderId) => {
    const isDesc = orderBy === property && order === 'desc';
    setOrder(isDesc ? 'asc' : 'desc');
    setOrderBy(property);
  };

  const handleChangePage = (_event: any, newPage: number) => {
    setPage(newPage);
  };

  return (
    <>
      <Table>
        <SendTableHeader
          order={order}
          orderBy={orderBy}
          onRequestSort={handleSortRequest}
        />
        <TableBody>
          {stableSort(sends, getSorting(order, orderBy))
            .slice(page * ROWS_PER_PAGE, (page + 1) * ROWS_PER_PAGE)
            .map(s => (
              <TableRow key={`${s.name}-${s.location}`}>
                <TableCell component="th" scope="row">{s.name}</TableCell>
                <TableCell align="right">{s.style}</TableCell>
                <TableCell align="right">{s.grade}</TableCell>
                <TableCell align="right">{s.tickType}</TableCell>
                <TableCell align="right">{s.location}</TableCell>
              </TableRow>
            ))}
        </TableBody>
      </Table>
      <TablePagination
        component="div"
        count={sends.length}
        rowsPerPage={ROWS_PER_PAGE}
        rowsPerPageOptions={[]}
        page={page}
        onChangePage={handleChangePage}
      />
    </>
  );
}
