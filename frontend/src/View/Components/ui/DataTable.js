// src/components/filter.table.js
import React from "react";

import {useTable, useFilters, useGlobalFilter, useAsyncDebounce, usePagination, useSortBy} from 'react-table'
import Button from "./Button";
import SweetAlert from "react-bootstrap-sweetalert";
import {useTranslation, withTranslation} from "react-i18next";


// Define a default UI for filtering
function GlobalFilter({globalFilter, setGlobalFilter}) {
    const [value, setValue] = React.useState(globalFilter)
    const onChange = useAsyncDebounce(value => {
        setGlobalFilter(value || undefined)
    }, 200)

    return (
        <span>
            Global search:{' '}
            <input
                className="form-control"
                value={value || ""}
                onChange={e => {
                    setValue(e.target.value);
                    onChange(e.target.value);
                }}
            />
        </span>
    )
}

function DefaultColumnFilter({column: {filterValue, setFilter}}) {
    return (
        <input
            className="form-control"
            value={filterValue || ''}
            onChange={e => {
                setFilter(e.target.value || undefined)
            }}
        />
    )
}

function Table({columns, data, operations}) {
    const [deleteClicked, setDeleteClicked] = React.useState(false)
    const [id, setId] = React.useState(-1)
    const [url, setUrl] = React.useState("")

    const defaultColumn = React.useMemo(() => ({Filter: DefaultColumnFilter}), [])

    const {t} = useTranslation();

    const {
        getTableProps,
        getTableBodyProps,
        headerGroups,
        prepareRow,
        page,
        canPreviousPage,
        canNextPage,
        pageOptions,
        pageCount,
        gotoPage,
        nextPage,
        previousPage,
        setPageSize,
        state: {pageIndex, pageSize},
        state,
        preGlobalFilteredRows,
        setGlobalFilter,
    } = useTable(
        {
            columns,
            data,
            defaultColumn,
            initialState: {pageIndex: 0, pageSize: 10}
        },
        useFilters,
        useGlobalFilter,
        useSortBy,
        usePagination,
    )

    function handleDeleteOperation(url, elementId) {
        setId(elementId)
        setUrl(url.replace("{id}", elementId))
        setDeleteClicked(true)
    }

    return (
        <div>
            <GlobalFilter
                preGlobalFilteredRows={preGlobalFilteredRows}
                globalFilter={state.globalFilter}
                setGlobalFilter={setGlobalFilter}
            />
            <table className="table" {...getTableProps()}>
                <thead>
                {headerGroups.map(headerGroup => (
                    <tr {...headerGroup.getHeaderGroupProps()}>
                        {headerGroup.headers.map(column => (
                            <div style={{display: "table-cell"}}>
                                <th {...column.getHeaderProps(column.getSortByToggleProps())}>
                                <span>
                                    {column.render('Header')}
                                    {column.isSorted
                                        ? column.isSortedDesc
                                            ? ' 🔽'
                                            : ' 🔼'
                                        : ''}
                                </span>
                                </th>
                                {column.canFilter ? column.render('Filter') : null}
                            </div>
                        ))}
                    </tr>
                ))}
                </thead>
                <tbody {...getTableBodyProps()}>
                {page.map((row) => {
                    console.log(page)
                    prepareRow(row)
                    return (
                        <tr {...row.getRowProps()}>
                            {row.cells.map(cell => {
                                return <td {...cell.getCellProps()}>{cell.render('Cell')}</td>
                            })}
                            {operations.map(operation => {
                                return <td><Button
                                    className={operation.className}
                                    text={operation.name}
                                    onClick={() => {
                                        if (operation.name === 'Delete') {
                                            handleDeleteOperation(operation.url,
                                                row.original[operation.onClickPassParameter])
                                        } else {
                                            operation.onClick(row.original[operation.onClickPassParameter])
                                        }
                                    }}
                                /></td>
                            })}
                        </tr>
                    )
                })}
                </tbody>
            </table>
            <ul className="pagination">
                <li className="page-item" onClick={() => gotoPage(0)} disabled={!canPreviousPage}>
                    <a className="page-link">First</a>
                </li>
                <li className="page-item" onClick={() => previousPage()} disabled={!canPreviousPage}>
                    <a className="page-link">{'<'}</a>
                </li>
                <li className="page-item" onClick={() => nextPage()} disabled={!canNextPage}>
                    <a className="page-link">{'>'}</a>
                </li>
                <li className="page-item" onClick={() => gotoPage(pageCount - 1)} disabled={!canNextPage}>
                    <a className="page-link">Last</a>
                </li>
                <li>
                    <a className="page-link">
                        Page{' '}
                        <strong>
                            {pageIndex + 1} of {pageOptions.length}
                        </strong>{' '}
                    </a>
                </li>
                <li>
                    <a className="page-link">
                        <input
                            className="form-control"
                            type="number"
                            min={1}
                            max={pageOptions.length}
                            defaultValue={pageIndex + 1}
                            onChange={e => {
                                const page = e.target.value ? Number(e.target.value) - 1 : 0
                                gotoPage(page)
                            }}
                            style={{width: '100px', height: '20px'}}
                        />
                    </a>
                </li>
                {' '}
                <select
                    className="form-control"
                    value={pageSize}
                    onChange={e => {
                        setPageSize(Number(e.target.value))
                    }}
                    style={{width: '120px', height: '38px'}}
                >
                    {[5, 10, 20, 30, 40, 50].map(pageSize => (
                        <option key={pageSize} value={pageSize}>
                            Show {pageSize}
                        </option>
                    ))}
                </select>
            </ul>
            {deleteClicked && <SweetAlert
                danger
                dependencies={[deleteClicked]}
                title={t("AreYouSure")}
                customButtons={
                    <React.Fragment>
                        <button
                            className="w3-btn w3-light-grey w3-round-small w3-medium"
                            onClick={() => setDeleteClicked(false)}
                        >{"Cancel"}</button>
                        &nbsp;
                        <button
                            className="w3-btn w3-red w3-round-small w3-medium"
                            onClick={() => {
                                alert("Deleted " + id + " on path " + url)
                                setDeleteClicked(false)
                            }
                            }
                        >{"Delete"}</button>
                    </React.Fragment>
                }
            >
                {"Cannot recover"}
            </SweetAlert>}
        </div>
    )
}

function DataTableComponent() {
    const columns = React.useMemo(
        () => [
            {
                Header: 'ID',
                accessor: 'id',
            },
            {
                Header: 'First Name',
                accessor: 'firstName',
            },
            {
                Header: 'Last Name',
                accessor: 'lastName'
            },
            {
                Header: 'Age',
                accessor: 'age'
            },
            {
                Header: 'Visits',
                accessor: 'visits'
            },
            {
                Header: 'Status',
                accessor: 'status'
            },
            {
                Header: 'Profile Progress',
                accessor: 'progress'
            }
        ],
        []
    )

    const data = [
        {
            "id": 1,
            "firstName": "horn-od926",
            "lastName": "selection-gsykp",
            "age": 22,
            "visits": 20,
            "progress": 39,
            "status": "single"
        },
        {
            "id": 2,
            "firstName": "heart-nff6w",
            "lastName": "information-nyp92",
            "age": 16,
            "visits": 98,
            "progress": 40,
            "status": "complicated"
        },
        {
            "id": 3,
            "firstName": "minute-yri12",
            "lastName": "fairies-iutct",
            "age": 7,
            "visits": 77,
            "progress": 39,
            "status": "single"
        },
        {
            "id": 4,
            "firstName": "degree-jx4h0",
            "lastName": "man-u2y40",
            "age": 27,
            "visits": 54,
            "progress": 92,
            "status": "relationship"
        },
        {
            "id": 5,
            "firstName": "horn-od926",
            "lastName": "selection-gsykp",
            "age": 22,
            "visits": 20,
            "progress": 39,
            "status": "single"
        },
        {
            "id": 6,
            "firstName": "heart-nff6w",
            "lastName": "information-nyp92",
            "age": 16,
            "visits": 98,
            "progress": 40,
            "status": "complicated"
        },
        {
            "id": 7,
            "firstName": "minute-yri12",
            "lastName": "fairies-iutct",
            "age": 7,
            "visits": 77,
            "progress": 39,
            "status": "single"
        },
        {
            "id": 8,
            "firstName": "degree-jx4h0",
            "lastName": "man-u2y40",
            "age": 27,
            "visits": 54,
            "progress": 92,
            "status": "relationship"
        }
    ]

    const operations = [
        {
            "name": "Delete",
            "className": "w3-btn w3-red w3-round-small w3-medium",
            "onClickPassParameter": "id",
            "url": "placement-owners/placements/{id}",
        },
        {
            "name": "Edit",
            "onClick": editRoom,
            "className": "w3-btn w3-khaki w3-round-small w3-medium",
            "onClickPassParameter": "id"
        },
    ]

    return (
        <Table columns={columns} data={data} operations={operations}/>
    )
}

function deleteRoom(id) {
    alert("Deleted " + id)
}

function editRoom(id) {
    alert("Edited " + id)
}

export default withTranslation()(DataTableComponent);
