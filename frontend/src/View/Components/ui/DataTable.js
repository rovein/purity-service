// src/components/filter.table.js
import React from "react";

import {useTable, useFilters, useGlobalFilter, useAsyncDebounce, usePagination, useSortBy} from 'react-table'
import Button from "./Button";
import SweetAlert from "react-bootstrap-sweetalert";
import {useTranslation, withTranslation} from "react-i18next";

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
            style={{"width": "100px"}}
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
    const columnStyle = {verticalAlign: "middle"};

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
        <div className={"w3-responsive"}>
            <GlobalFilter
                preGlobalFilteredRows={preGlobalFilteredRows}
                globalFilter={state.globalFilter}
                setGlobalFilter={setGlobalFilter}
            />
            <table className="w3-table-all w3-large w3-centered w3-hoverable" {...getTableProps()}>
                <thead>
                {headerGroups.map(headerGroup => (
                    <>
                        <tr {...headerGroup.getHeaderGroupProps()} className={"w3-light-grey"}>
                            {headerGroup.headers.map(column => (
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
                            ))}
                        </tr>
                        <tr {...headerGroup.getHeaderGroupProps()} className={"w3-light-grey"}>
                            {headerGroup.headers.map(column => (
                                <th>
                                    {column.canFilter ? column.render('Filter') : null}
                                </th>
                            ))}
                        </tr>
                    </>
                ))}
                </thead>
                <tbody {...getTableBodyProps()}>
                {page.map((row) => {
                    console.log(page)
                    prepareRow(row)
                    return (
                        <tr {...row.getRowProps()} className="w3-hover-sand">
                            {row.cells.map(cell => {
                                return <td style={columnStyle} {...cell.getCellProps()}>{cell.render('Cell')}</td>
                            })}
                            <td style={columnStyle}>
                                {operations.map(operation => {
                                    return <><Button
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
                                    /> &nbsp;
                                    </>
                                })}
                            </td>
                        </tr>
                    )
                })}
                </tbody>
            </table>
            <div className="w3-center">
                <ul className="w3-bar w3-large">
                    <li className="w3-button w3-bar-item w3-hover-light-grey" onClick={() => gotoPage(0)}
                        disabled={!canPreviousPage}>
                        <a>{'<<'}</a>
                    </li>
                    <li className="w3-button w3-bar-item w3-hover-light-grey" onClick={() => previousPage()}
                        disabled={!canPreviousPage}>
                        <a>{'<'}</a>
                    </li>
                    <li className="w3-button w3-bar-item w3-hover-light-grey" onClick={() => nextPage()}
                        disabled={!canNextPage}>
                        <a>{'>'}</a>
                    </li>
                    <li className="w3-button w3-bar-item w3-hover-light-grey" onClick={() => gotoPage(pageCount - 1)}
                        disabled={!canNextPage}>
                        <a>{'>>'}</a>
                    </li>
                    <li className="w3-button w3-bar-item w3-hover-light-grey">
                        <a>
                            Page{' '}
                            <strong>
                                {pageIndex + 1} of {pageOptions.length}
                            </strong>{' '}
                        </a>
                    </li>
                    <li className="w3-button w3-bar-item w3-hover-light-grey">
                        <a>
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
                        className="w3-button w3-bar-item w3-hover-light-grey"
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
            </div>
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

function deleteRoom(id) {
    alert("Deleted " + id)
}

function editRoom(id) {
    alert("Edited " + id)
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

export default withTranslation()(DataTableComponent);
