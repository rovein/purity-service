import React, {useEffect, useState} from "react";

import {
    useTable,
    useFilters,
    useGlobalFilter,
    useAsyncDebounce,
    usePagination,
    useSortBy
} from 'react-table'
import Button from "./Button";
import SweetAlert from "react-bootstrap-sweetalert";
import {useTranslation, withTranslation} from "react-i18next";
import axios from '../util/ApiUtil';
import * as Constants from "../util/Constants";
import DefaultLoader from "./Loader";
import doWithDelay from "../util/DelayUtil";

const baseUrl = Constants.SERVER_URL;

function GlobalFilter({globalFilter, setGlobalFilter, searchPlaceholder}) {
    const {t} = useTranslation();
    const [value, setValue] = React.useState(globalFilter)
    const onChange = useAsyncDebounce(value => {
        setGlobalFilter(value || undefined)
    }, 200)

    return (
        <span>
            <input
                className={"w3-input w3-border-bottom w3-border-black w3-hover-sand"}
                placeholder={t(searchPlaceholder)}
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
            value={filterValue || ''}
            style={{"width": "100px"}}
            onChange={e => {
                setFilter(e.target.value || undefined)
            }}
        />
    )
}

function Table({columns, data, operations, searchPlaceholder}) {
    const [id, setId] = React.useState(-1)
    const [deleteClicked, setDeleteClicked] = React.useState(false)
    const [deleteUrl, setDeleteUrl] = React.useState("")
    const [deleteCallback, setDeleteCallback] = React.useState(() => (id) => console.log(id))
    const [isLoaded, setIsLoaded] = useState(true)

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
            initialState: {pageIndex: 0, pageSize: 10},
            autoResetRowState: true,
            autoResetPage: true
        },
        useFilters,
        useGlobalFilter,
        useSortBy,
        usePagination,
    )

    function handleDeleteOperation(url, elementId, callback) {
        setId(elementId)
        setDeleteUrl(url.replace("{id}", elementId))
        setDeleteCallback(() => callback)
        setDeleteClicked(true)
    }

    function deleteEntity(url, id) {
        axios.delete(`${baseUrl}/${url}`)
            .then(result => {
               doWithDelay(() => {
                    deleteCallback(id)
                    setIsLoaded(true)
                })
        }).catch(e => {
            alert(e)
        })
    }

    if (!isLoaded) return <DefaultLoader/>;
    return (
        <div className={"w3-responsive"}>
            <GlobalFilter
                preGlobalFilteredRows={preGlobalFilteredRows}
                globalFilter={state.globalFilter}
                setGlobalFilter={setGlobalFilter}
                searchPlaceholder={searchPlaceholder}
            />
            <table className="w3-table-all w3-large w3-centered w3-hoverable" {...getTableProps()}>
                <thead>
                {headerGroups.map(headerGroup => (
                    <>
                        <tr {...headerGroup.getHeaderGroupProps()} className={"w3-light-grey"}>
                            {headerGroup.headers.map(column => (
                                <th {...column.getHeaderProps(column.getSortByToggleProps())}>
                                <span>
                                    {t(column.render('Header'))}
                                    {column.isSorted
                                        ? column.isSortedDesc
                                            ? ' 🔽'
                                            : ' 🔼'
                                        : ''}
                                </span>
                                </th>
                            ))}
                            {operations.length === 0 ? <></> :  <th> </th>}
                        </tr>
                        <tr {...headerGroup.getHeaderGroupProps()} className={"w3-light-grey"}>
                            {headerGroup.headers.map(column =>
                                <th className={"w3-border-bottom w3-border-black"}>
                                    {column.canFilter && column.id !== 'actions' ? column.render('Filter') : null}
                                </th>
                            )}
                            {operations.length === 0 ? <></> : <th className={"w3-border-bottom w3-border-black"}></th>}
                        </tr>
                    </>
                ))}
                </thead>
                <tbody {...getTableBodyProps()}>
                {page.map((row) => {
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
                                        text={t(operation.name)}
                                        onClick={() => {
                                            if (operation.name === 'Delete') {
                                                handleDeleteOperation(operation.url,
                                                    row.original[operation.onClickPassParameter], operation.onClick)
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
                            {t("Page")}{' '}
                            <strong>
                                {pageIndex + 1} / {pageOptions.length}
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
                        style={{width: '180px', height: '38px'}}
                    >
                        {[5, 10, 20, 30, 40, 50].map(pageSize => (
                            <option key={pageSize} value={pageSize}>
                                {t("Show")} {pageSize}
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
                        >{t("Cancel")}</button>
                        &nbsp;
                        <button
                            className="w3-btn w3-red w3-round-small w3-medium"
                            onClick={() => {
                                setDeleteClicked(false)
                                setIsLoaded(false)
                                deleteEntity(deleteUrl, id)
                            }
                            }
                        >{t("Delete")}</button>
                    </React.Fragment>
                }
            >
                {t("NotRecover")}
            </SweetAlert>}
        </div>
    )
}

function DataTableComponent({displayData, displayColumns, operations, searchPlaceholder}) {

    const [data, setData] = useState(displayData)

    const columns = React.useMemo(() => [...displayColumns], [])

    function deleteEntity(id) {
        let index = data.findIndex(entry => entry.id == id);
        data.splice(index, 1)
        setData(data.filter(entry => entry.id != id))
    }

    useEffect(() => {
        operations.forEach(operation => {
            if (operation.name === "Delete") {
                operation.onClick = deleteEntity
            }
        })
    }, [])

    return (
        <Table columns={columns} data={data} operations={operations} searchPlaceholder={searchPlaceholder}/>
    )
}

export default withTranslation()(DataTableComponent);
