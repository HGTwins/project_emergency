'use client'
import React, { useEffect } from "react";
import { useRouter } from "next/navigation";
export default function OAuth2Callback() {

    const router = useRouter();

    useEffect(() => {
        const params = new URLSearchParams(window.location.search);
        const token = params.get("token"); // URL에서 token 추출
        const username = params.get("username");

        if (token && username) {
            sessionStorage.setItem("jwtToken", "Bearer " + token);
            sessionStorage.setItem("username", username);
            sessionStorage.setItem("role", "ROLE_MEMBER");

            const fetchCallback = async () => {

                const resp = await fetch(`https://nonefficient-lezlie-progressively.ngrok-free.dev/api/getMember/${username}`)
                const data = await resp.json()
                const alias = data.alias;

                sessionStorage.setItem('alias', alias!);

                alert("로그인 성공!");

                // 로그인 성공 로직 내부
                const redirectUrl = sessionStorage.getItem('redirectUrl');

                if (redirectUrl) {
                    // 저장된 URL이 있으면 해당 페이지로 이동 후 데이터 삭제
                    sessionStorage.removeItem('redirectUrl');
                    router.push(redirectUrl);
                } else {
                    // 저장된 URL이 없으면 기본 페이지(예: 메인)로 이동
                    router.push('/medicalInfo');
                }
            };
            fetchCallback();
        } else {
        alert("인증 정보가 없습니다.");
        router.push("/");
    }
    }, []);
    return <p>로그인 처리 중...</p>;
};
